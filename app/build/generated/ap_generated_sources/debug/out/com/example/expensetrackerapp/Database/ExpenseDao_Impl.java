package com.example.expensetrackerapp.Database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExpenseEntity> __insertAdapterOfExpenseEntity;

  private final EntityDeleteOrUpdateAdapter<ExpenseEntity> __deleteAdapterOfExpenseEntity;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExpenseEntity = new EntityInsertAdapter<ExpenseEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `expenses` (`id`,`date`,`category`,`amount`,`description`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDate() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDate());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getCategory());
        }
        statement.bindLong(4, entity.getAmount());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getDescription());
        }
      }
    };
    this.__deleteAdapterOfExpenseEntity = new EntityDeleteOrUpdateAdapter<ExpenseEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public void insertExpense(final ExpenseEntity expense) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExpenseEntity.insert(_connection, expense);
      return null;
    });
  }

  @Override
  public void deleteExpense(final ExpenseEntity expense) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExpenseEntity.handle(_connection, expense);
      return null;
    });
  }

  @Override
  public LiveData<List<ExpenseEntity>> getAllExpenses() {
    final String _sql = "SELECT * FROM expenses ORDER BY date DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "category");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>();
        while (_stmt.step()) {
          final ExpenseEntity _item;
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final String _tmpCategory;
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null;
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory);
          }
          final int _tmpAmount;
          _tmpAmount = (int) (_stmt.getLong(_columnIndexOfAmount));
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _item = new ExpenseEntity(_tmpDate,_tmpCategory,_tmpAmount,_tmpDescription);
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<ExpenseEntity>> getLastThreeExpenses() {
    final String _sql = "SELECT * FROM expenses ORDER BY date DESC LIMIT 3";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "category");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>();
        while (_stmt.step()) {
          final ExpenseEntity _item;
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final String _tmpCategory;
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null;
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory);
          }
          final int _tmpAmount;
          _tmpAmount = (int) (_stmt.getLong(_columnIndexOfAmount));
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          _item = new ExpenseEntity(_tmpDate,_tmpCategory,_tmpAmount,_tmpDescription);
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
