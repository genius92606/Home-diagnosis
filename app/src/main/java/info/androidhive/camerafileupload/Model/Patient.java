package info.androidhive.camerafileupload.Model;

public class Patient {
    String Nama,Score,Time;

    public Patient(String nama, String score, String time){

        Nama=nama;Score=score;Time=time;

    }

    public String getNama(){
        return Nama;
    }

    public String getScore(){
        return Score;
    }

    public String getTime(){
        return Time;
    }
}
