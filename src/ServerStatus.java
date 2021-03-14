class ServerStatus {
    public boolean isOnline;
    public long changed;

    public ServerStatus(boolean isOnline, long changed) {
        this.isOnline = isOnline;
        this.changed = changed;
    }
}
