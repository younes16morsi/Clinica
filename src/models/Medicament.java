package models;

import core.DB;
import sun.security.provider.MD2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Medicament {

    //attributes:
    private int id;
    private String nomCommercial;
    private String nomSientifique;
    private String type;

    public Medicament(){}

    public Medicament(String nomCommercial, String nomSientifique, String type) {
        this.nomCommercial = nomCommercial;
        this.nomSientifique = nomSientifique;
        this.type = type;
    }

    //setters getters:
    public int getId() {
        return id;
    }

    public String getNomSientifique() {
        return nomSientifique;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public void setNomSientifique(String nomSientifique) {
        this.nomSientifique = nomSientifique;
    }

    //tested methodes:

    public void afficher(){
        System.out.println("nom commercial: "+nomCommercial+" nom sien: "+nomSientifique+" type: "+type);
    }
    //get medicament de la BDD, si
    public Medicament getMedicament(int idMedicament){
        Medicament medicament = new Medicament();
        ResultSet resultSet = DB.query("select * from MEDICAMENT where id = "+idMedicament);

        try{
            if(resultSet.next()){
                medicament.setNomCommercial(resultSet.getString("NOM_COMMERCIAL"));
                medicament.setType(resultSet.getString("TYPE"));
                medicament.setNomSientifique(resultSet.getString("NOM_SCIENTIFIQUE"));
            }
        }catch (SQLException e ){
            System.out.println("[ERROR]: MEDICAMENT.getMedicament: "+e.getMessage());
        }finally {
            return medicament;
        }
    }
    //inserer nouveau medicament a la base de donne et retourne son id.
    public static int insertNewMed(Medicament med){
        //PreparedStatement preparedStatement;
       // Connection connection = DB.getConnection();
        ResultSet resultSet1;
        try {

            DB.query("insert into medicament(nom_commercial, nom_scientifique, type) values(?,?,?) ", med.getNomCommercial(), med.getNomCommercial(),med.getType());
            resultSet1 = DB.query("select id from medicament order by id desc");
            if(resultSet1.next())
                return resultSet1.getInt("id");
            else{
                System.out.println("Medicament: insertNewMed(): resultSet1 has no getInt?...");
            }
        }catch (SQLException e){
            System.out.println("Medicament: insertNewMed(): "+e.getMessage());
        }
        return -1;
    }
    //sauvgarder le med en retournant son id, si le med existe deja retourner son id.
    public  int saveMed(){
        ResultSet resultSet;
       // Connection connection= DB.getConnection();

        if(exist(this)){
            resultSet = DB.query("SELECT * FROM MEDICAMENT WHERE NOM_COMMERCIAL='"+
                    this.getNomCommercial()+"'");
            try{
                resultSet.next();
                this.id= resultSet.getInt("ID");
                return  this.id;
            }catch (SQLException e){
                System.out.println("Medicament: saveMed(): "+e.getMessage());
            }
        }else{
            this.id = insertNewMed(this);
            return this.id;
        }
        return -1;
    }

    public static boolean exist(Medicament medicament){
        ResultSet resultSet = DB.query("SELECT NOM_COMMERCIAL FROM MEDICAMENT WHERE NOM_COMMERCIAL='"+
                medicament.getNomCommercial()+"'");
        if(resultSet!=null) {
            try {
                if (resultSet.next()) {
                    return true;
                }

            } catch (SQLException e) {
                System.out.println("Medicament: exist(): " + e.getMessage());
            }finally {
            }
        }
        return false;

    }

    //not yet tested methodes:
}
