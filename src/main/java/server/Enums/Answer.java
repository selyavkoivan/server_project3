package server.Enums;

public enum Answer {
    Error("e"), Success("s");
    private String code;
    Answer(String code){
        this.code = code;
    }
    @Override
    public String toString(){ return code;}
}
