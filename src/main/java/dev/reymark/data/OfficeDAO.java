package dev.reymark.data;

import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dev.reymark.App;
import dev.reymark.hr.Office;
import dev.sol.db.DBParam;
import dev.sol.db.DBService;
import javafx.collections.ObservableList;

public class OfficeDAO {
    public static final String TABLE = "office";
    public static final DBService DB = App.DB_COOP;

    private static final ObservableList<Office> officelist = App.COLLECTIONS_REGISTRY.getList("OFFICE");

    private static Office data(CachedRowSet crs) {
        try {
            String office_id = crs.getString("officeID");
            String office_name = crs.getString("officeName");

            return new Office(office_id, office_name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static DBParam[] paramlist(Office office) {
        List<DBParam> paramList = new LinkedList<>();
        paramList.add(new DBParam(Types.VARCHAR, "officeID", office.getOfficeId()));
        paramList.add(new DBParam(Types.VARCHAR, "officeName", office.getOfficeName()));
    }

    public static List<Office> getOfficeList() {
        CachedRowSet crs = DB.select_all(TABLE);
        List<Office> list = new LinkedList<>();

        try {
            while (crs.next()) {
                Office office = data(crs);
                if (office != null)
                    ;
                list.add(office);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        list.forEach(office -> {
            String officeName = office.getOfficeId().getOfficeName();
            if (!officeName.isEmpty())
                office.setOfficeName(
                        list.stream()
                                .filter(e -> e.getOfficeId().equals(officeName)));

            office.rebaseline();
        });
        return list;

    }

    public static void insert(Office office) {

        DB.insert(TABLE, paramlist(office));
    }

    public static void update(Office office) {
        DB.update(TABLE,
                new DBParam(Types.VARCHAR, "officeId", office.getOfficeId()),
                new DBParam(Types.VARCHAR, "officeName", office.getOfficeName()));
    }

    public static void delete(Office office) {
        DB.delete(TABLE, new DBParam(Types.VARCHAR, "officeId", office.getOfficeId()));
    }

}
