package logger;


public enum LOGTYPE {
    ERROR(1), INFO(2), WARNING(3), APPLICATION(4);

    int index;

    LOGTYPE(int i) {
        index = i;
    }
}

