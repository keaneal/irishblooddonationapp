package com.hotmail.keanser.irishblooddonationapp.questionnaire;

public class Questionnaire {
	
    //private variables
    int _qnum;
    String _question;
//    String _answer;
    String _response;
    
     
    // Empty constructor
    public Questionnaire(){
         
    }
    // constructor
    public Questionnaire(int qnum, String question, String response){
        this._qnum = qnum;
        this._question = question;
//        this._answer = answer;
        this._response = response;
    }
     
//    // constructor
//    public Questionnaire(String question, String answer, String response){
//        this._question = question;
//        this._answer = answer;
//        this._response = response;
//    }
    // getting qnum
    public int getQnum(){
        return this._qnum;
    }
     
    // setting qnum
    public void setQnum(int qnum){
        this._qnum = qnum;
    }
     
    // getting question
    public String getQuestion(){
        return this._question;
    }
     
    // setting question
    public void setQuestion(String question){
        this._question = question;
    }
     
//    // getting answer
//    public String getAnswer(){
//        return this._answer;
//    }
//     
//    // setting answer
//    public void setAnswer(String answer){
//        this._answer = answer;
//    }
    
    // getting response
    public String getResponse(){
        return this._response;
    }
     
    // setting response
    public void setResponse(String response){
        this._response = response;
    }

}
