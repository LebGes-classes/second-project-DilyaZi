public class Worker {
    private int id;
    private String name;
    private boolean activity;  //  (может меняться)

    public Worker() {}
    public Worker(int id, String name, boolean activity) {
        this.id = id;
        this.name = name;
        this.activity = true;
    }

    public boolean workerActive(){
        return activity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getActivity() {
        return activity;
    }
    public void setActivity(boolean activity) {
        this.activity = activity;
    }
}
