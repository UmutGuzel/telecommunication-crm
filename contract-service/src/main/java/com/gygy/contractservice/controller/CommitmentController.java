package com.gygy.contractservice.controller;

import com.gygy.contractservice.dto.commitment.CommitmentListiningDto;
import com.gygy.contractservice.dto.commitment.CreateCommitmentDto;
import com.gygy.contractservice.dto.commitment.DeleteCommitmentDto;
import com.gygy.contractservice.dto.commitment.UpdateCommitmentDto;
import com.gygy.contractservice.service.CommitmentService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commitments")
public class CommitmentController {
    private final CommitmentService commitmentService;

    public CommitmentController(CommitmentService commitmentService) {
        this.commitmentService = commitmentService;
    }

    @GetMapping
    public List<CommitmentListiningDto> getAll() {
        return commitmentService.getAll();
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CreateCommitmentDto createCommitmentDto) {
        commitmentService.add(createCommitmentDto);
    }
    @PutMapping
    public void update(@RequestBody UpdateCommitmentDto updateCommitmentDto) {
        commitmentService.update(updateCommitmentDto);
    }
    @DeleteMapping
    public void delete(@RequestBody DeleteCommitmentDto deleteCommitmentDto) {
        commitmentService.delete(deleteCommitmentDto);
    }
}
