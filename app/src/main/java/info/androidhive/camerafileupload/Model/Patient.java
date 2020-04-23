package info.androidhive.camerafileupload.Model;

public class Patient {
    String Nama,Complete_score,Fluency_score,Time;

    public Patient(String nama, String complete_score,String fluency_score, String time){

        Nama=nama;Complete_score=complete_score;Fluency_score=fluency_score;Time=time;

    }

    public String getNama(){
        return Nama;
    }

    public String getCompleteScore(){
        return Complete_score;
    }
    public String getFluencyScore(){
        return Fluency_score;
    }

    public String getTime(){
        return Time;
    }

}
