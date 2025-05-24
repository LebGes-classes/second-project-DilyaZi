import java.util.ArrayList;
import java.util.List;

//управление складом
public class Warehouse {
    private int id;
    private List<StorageCell> cells;
    private boolean isOpen;

    public Warehouse(int id) {
        this.id = id;
        this.cells = new ArrayList<>();
        this.isOpen = true;
    }

    public void addCell(StorageCell cell) {
        cells.add(cell);
    }

    public void closeWarehouse() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<StorageCell> getCells() {
        return cells;
    }

    public int getId() {
        return id;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }
}
