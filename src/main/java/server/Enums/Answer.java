package server.Enums;

public enum Answer {
    ERROR("e"), SUCCESS("s");
    private String code;
    Answer(String code){
        this.code = code;
    }
    @Override
    public String toString(){ return code;}
}
