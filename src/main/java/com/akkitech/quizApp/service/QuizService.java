package com.akkitech.quizApp.service;

import com.akkitech.quizApp.dao.QuestionDao;
import com.akkitech.quizApp.dao.QuizDao;
import com.akkitech.quizApp.model.Question;
import com.akkitech.quizApp.model.QuestionWrapper;
import com.akkitech.quizApp.model.Quiz;
import com.akkitech.quizApp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<String> createQuiz(String category, int numQue, String title) {
        try {
            List<Question> questionList = questionDao.findRandomQuesByCategory(category, numQue);
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionList(questionList);
            quizDao.save(quiz);
            return new ResponseEntity<>("Quiz Created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Quiz Creation Failed",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
//        Service method for returning quiz questions
        Optional<Quiz> quiz = quizDao.findById(id);

        List<Question> questionListFromDB = quiz.get().getQuestionList();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();

        for(Question q: questionListFromDB){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<String> calculateResult(int quizId, List<Response> responses) {
        int result = 0;
        Optional<Quiz> quiz = quizDao.findById(quizId);
        List<Question> questionList = quiz.get().getQuestionList();

        int i=0;
        for(Response response: responses){
            if(response.getResponse().equals(questionList.get(i).getRightAnswer())){
                result++;
            }
            i++;
        }

        return new ResponseEntity<>("You have attempted "+result+" correct answers", HttpStatus.OK);
    }
}
