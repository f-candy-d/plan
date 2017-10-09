package com.f_candy_d.plan.data.source.local.table;

import com.f_candy_d.plan.data.model.Plan;
import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.ColumnDataType;
import com.f_candy_d.sqliteutils.TableUtils;

/**
 * Created by daichi on 10/10/17.
 */

public final class PlanTable extends BaseTable {

    private PlanTable() {}

    public static final String TABLE_NAME = Plan.MODEL_NAME;

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    public static final String _TITLE = "title";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    public static TableUtils.TableSource getTableSource() {
        return getBaseTableSource(TABLE_NAME)
                .put(_TITLE, ColumnDataType.TEXT);
    }
}
