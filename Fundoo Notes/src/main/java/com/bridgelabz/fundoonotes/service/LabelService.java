package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Response;

import java.util.Set;

public interface LabelService {
    Label create(LabelDto labelDto, String token);
    Response update(LabelDto labelDto, int id, String token);
    Response delete(int id, String token);
    Set<Label> getAllLabels(String token);
    Label getLabel(int id, String token) ;
}
