package com.bridgelabz.fundoonotes.controller;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.LabelException;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping
    public ResponseEntity<Object> addLabel(@Valid @RequestBody LabelDto labelDto, @RequestHeader String token) {

        Object response;
        try {
            if(labelDto.getName().equals(""))
                response = ResponseInfo.getResponse(-800, "label name can't not be empty");
            else
                response = labelService.create(labelDto, token);
        }
        catch(UserException e) {
            response = ResponseInfo.getResponse(e.getErrorCode(),e.getMessage());
        }
        catch(LabelException e) {
            response = ResponseInfo.getResponse(e.getErrorCode(),e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateLabel(@Valid @RequestBody LabelDto labelDto, @PathVariable int id, @RequestHeader String token) {
        Response response = null;
        if(labelDto.getName().equals(""))
            response = ResponseInfo.getResponse(-800, "label name can't not be empty");
        else
            response =labelService.update(labelDto, id, token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteLabel(@PathVariable int id,@RequestHeader String token) {
        Response response =labelService.delete(id, token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/getLabel")
    public ResponseEntity<Object> getAllLabelsOfUser(@RequestHeader String token) {
        Object response;
        try {
            response = labelService.getAllLabels(token);
        }
        catch(UserException e) {
            response = ResponseInfo.getResponse(e.getErrorCode(),e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLabel(@PathVariable int id, @RequestHeader String token) {
        Object response;
        try {
            response = labelService.getLabel(id, token);
        }
        catch(UserException e) {
            response = ResponseInfo.getResponse(e.getErrorCode(),e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
