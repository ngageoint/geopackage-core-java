package mil.nga.geopackage.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mil.nga.geopackage.GeoPackageException;

/**
 * User Row Sync to support sharing a single user row read copy when multiple
 * near simultaneous asynchronous requests are made
 * 
 * @param <TColumn>
 * @param <TTable>
 * @param <TRow>
 * 
 * @author osbornb
 * @since 1.3.2
 */
public class UserRowSync<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>> {

	/**
	 * Synchronous lock
	 */
	protected Lock lock = new ReentrantLock();

	/**
	 * Mapping between row ids and row conditions
	 */
	protected Map<Long, RowCondition> rows = new HashMap<>();

	/**
	 * Condition and row wrapper
	 */
	protected class RowCondition {

		/**
		 * Wait and signal condition
		 */
		private Condition condition;

		/**
		 * Row
		 */
		private TRow row;

	}

	/**
	 * Constructor
	 */
	protected UserRowSync() {

	}

	/**
	 * Get the row if another same id request has been made by waiting until the
	 * row has been set. If no current request, lock the for the calling thread
	 * which should read the row and call {@link #setRow(long, UserCoreRow)}
	 * when complete.
	 * 
	 * @param id
	 *            user row id
	 * @return row if retrieved from a previous request, null if calling thread
	 *         should read row and set using {@link #setRow(long, UserCoreRow)}
	 */
	public TRow getRowOrLock(long id) {

		TRow row = null;

		lock.lock();
		try {
			RowCondition rowCondition = rows.get(id);
			if (rowCondition != null) {
				row = rowCondition.row;
				if (row == null) {
					// Another thread is currently retrieving the row, wait
					rowCondition.condition.await();

					// Row has now been retrieved
					row = rowCondition.row;
				}
			} else {
				// Set the row condition and the calling thread is now
				// responsible for retrieving the row
				rowCondition = new RowCondition();
				rowCondition.condition = lock.newCondition();
				rows.put(id, rowCondition);
			}
		} catch (InterruptedException e) {
			throw new GeoPackageException(
					"Interruption obtaining cached row or row lock. id: " + id,
					e);
		} finally {
			lock.unlock();
		}

		return row;
	}

	/**
	 * Set the row id, row, and notify all waiting threads to retrieve the row.
	 * 
	 * @param id
	 *            user row id
	 * @param row
	 *            user row or null
	 */
	public void setRow(long id, TRow row) {

		lock.lock();
		try {
			RowCondition rowCondition = rows.remove(id);
			if (rowCondition != null) {
				rowCondition.row = row;
				rowCondition.condition.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

}
