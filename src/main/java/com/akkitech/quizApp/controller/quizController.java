package com.akkitech.quizApp.controller;

import com.akkitech.quizApp.model.QuestionWrapper;
import com.akkitech.quizApp.model.Response;
import com.akkitech.quizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class quizController {

    @Autowired
    QuizService quizService;

    @GetMapping("getQuiz/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable int id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQue,
                                           @RequestParam String title){
        return quizService.createQuiz(category, numQue, title);
    }

    @PostMapping("submit/{quizId}")
    public ResponseEntity<String> submitQuiz(@PathVariable int quizId, @RequestBody List<Response> responses ){
        return quizService.calculateResult(quizId, responses);
    }
}
