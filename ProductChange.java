import java.sql.Timestamp;

public class ProductChange {
    private int productId;
    private String columnName;
    private String oldValue;
    private String newValue;
    private Timestamp changeTimestamp;

    public ProductChange(int productId, String columnName, String oldValue, String newValue) {
        this.productId = productId;
        this.columnName = columnName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeTimestamp = new Timestamp(System.currentTimeMillis()); // Set the current timestamp
    }

    public int getProductId() {
        return productId;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    // Add getters for other fields if needed

    @Override
    public String toString() {
        return "ProductChange{" +
                "productId=" + productId +
                ", columnName='" + columnName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", changeTimestamp=" + changeTimestamp +
                '}';
    }

    public Object getChangeTimestamp() {
        return null;
    }
}
