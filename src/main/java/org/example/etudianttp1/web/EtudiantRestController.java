package org.example.etudianttp1.web;

import org.example.etudianttp1.dto.RequestEtudiantDto;
import org.example.etudianttp1.dto.ResponseEtudiantDto;
import org.example.etudianttp1.service.EtudiantServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/etudiants")
public class EtudiantRestController {
    private EtudiantServiceImpl etudiantService;

    public EtudiantRestController(EtudiantServiceImpl etudiantService) {
        this.etudiantService = etudiantService;
    }
    @PostMapping
    public ResponseEntity<ResponseEtudiantDto> add (@RequestBody RequestEtudiantDto requestEtudiantDto)
    {
        ResponseEtudiantDto responseEtudiantDto =  etudiantService.addEtudiant(requestEtudiantDto);
        return ResponseEntity.ok(responseEtudiantDto);
    }
    @GetMapping
    public ResponseEntity <List<ResponseEtudiantDto>> getall(){
        List<ResponseEtudiantDto> etudiantDtos = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiantDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity <ResponseEtudiantDto> get(@PathVariable int id){
        ResponseEtudiantDto responseEtudiantDto = etudiantService.getEtudiantById(id);
        return ResponseEntity.ok(responseEtudiantDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseEtudiantDto> update(@PathVariable int id, @RequestBody RequestEtudiantDto dto) {
        ResponseEtudiantDto response = etudiantService.updateEtudiant(dto, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }
}
