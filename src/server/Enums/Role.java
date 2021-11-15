package server.Enums;

public enum  Role {
    Admin("a"), User("u"), Error("e");
    private String code;
    Role(String code){
        this.code = code;
    }
    @Override
    public String toString(){ return code;}
}
