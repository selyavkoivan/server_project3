package server.Enums;

public enum  Role {
    ADMIN("a"), USER("u"), ERROR("e");
    private String code;
    Role(String code){
        this.code = code;
    }
    @Override
    public String toString(){ return code;}
}
