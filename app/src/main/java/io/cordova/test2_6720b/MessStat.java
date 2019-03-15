package io.cordova.test2_6720b;



class MessStat {

    public int mess_count;
    public String readed;
    public String currtime;

    public MessStat() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public MessStat(int mess_count, String readed,String currtime) {
        this.mess_count = mess_count;
        this.readed = readed;
        this.currtime = currtime;

    }


}
