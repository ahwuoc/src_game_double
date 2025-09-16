package Top;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnecter;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import player.Player;
import item.Item;
import services.ItemService;

/**
 *
 * @author Admin
 */
public class TopVnd {

    @Getter
    private final List<Player> list = new ArrayList<>();
    private static final TopVnd INSTANCE = new TopVnd();

    public static TopVnd getInstance() {
        return INSTANCE;
    }

    public void load() {
        list.clear();
        String sql = """
                    SELECT p.id, p.name, p.head, p.gender, p.data_inventory,
                           p.data_point, p.items_body, a.tongnap
                    FROM player p
                    JOIN account a ON p.account_id = a.id
                    WHERE a.tongnap IS NOT NULL
                    ORDER BY a.tongnap DESC
                    LIMIT 100
                """;

        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(processPlayerResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Player processPlayerResultSet(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.id = rs.getInt("id");
        player.name = rs.getString("name");
        player.head = rs.getShort("head");
        player.gender = rs.getByte("gender");
        player.bktdeptrai = rs.getInt("tongnap");
        extractDataPoint(rs.getString("data_point"), player);
        extractItemsBody(rs.getString("items_body"), player);
        return player;
    }

    private void extractDataPoint(String dataPoint, Player player) {
        JSONArray dataArray = parseJsonArray(dataPoint);
        player.nPoint.power = toDouble(dataArray.get(11));
    }

    private void extractItemsBody(String itemsBody, Player player) {
        JSONArray dataArray = parseJsonArray(itemsBody);
        for (Object obj : dataArray) {
            player.inventory.itemsBody.add(createItemFromDataObject(obj.toString()));
        }
    }

    private Item createItemFromDataObject(String itemData) {
        JSONArray data = parseJsonArray(itemData);
        short itemId = toShort(data.get(0));
        if (itemId == -1)
            return ItemService.gI().createItemNull();

        Item item = ItemService.gI().createNewItem(itemId, toInt(data.get(1)));
        JSONArray optionArr = parseJsonArray(stripQuotes(data.get(2).toString()));
        for (Object option : optionArr) {
            JSONArray opt = parseJsonArray(option.toString());
            item.itemOptions.add(new Item.ItemOption(toInt(opt.get(0)), toInt(opt.get(1))));
        }

        item.createTime = toLong(data.get(3));
        if (ItemService.gI().isOutOfDateTime(item)) {
            return ItemService.gI().createItemNull();
        }
        return item;
    }

    // Helper methods
    private JSONArray parseJsonArray(String json) {
        return (JSONArray) new JSONValue().parse(json);
    }

    private String stripQuotes(String input) {
        return input.replaceAll("\"", "");
    }

    private int toInt(Object obj) {
        return Integer.parseInt(String.valueOf(obj));
    }

    private short toShort(Object obj) {
        return Short.parseShort(String.valueOf(obj));
    }

    private long toLong(Object obj) {
        return Long.parseLong(String.valueOf(obj));
    }

    private double toDouble(Object obj) {
        return Double.parseDouble(String.valueOf(obj));
    }
}