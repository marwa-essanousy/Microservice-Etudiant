package org.example.etudianttp1.service;

import org.example.etudianttp1.dto.FiliereDto;
import org.example.etudianttp1.dto.RequestEtudiantDto;
import org.example.etudianttp1.dto.ResponseEtudiantDto;
import org.example.etudianttp1.entities.Etudiant;
import org.example.etudianttp1.mappers.EtudiantMapper;
import org.example.etudianttp1.repository.EtudiantRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
@Service
public class EtudiantServiceImpl implements EtudiantService {
    private EtudiantRepository etudiantRepository;
    private EtudiantMapper etudiantMapper;
    private  WebClient filiereWebClient;
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper, WebClient filiereWebClient) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.filiereWebClient = filiereWebClient;
    }
    @Override
    public ResponseEtudiantDto addEtudiant(RequestEtudiantDto requestEtudiantDto) {
        Etudiant etudiant = etudiantMapper.DTO_To_Entity(requestEtudiantDto);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.Entity_To_DTO(savedEtudiant);
    }

    @Override
    public List<ResponseEtudiantDto> getAllEtudiants() {

        List <Etudiant> etudiantes = etudiantRepository.findAll();
        List <ResponseEtudiantDto> etudiantDtos = new ArrayList<>();
        for (Etudiant etudiant : etudiantes) {
            ResponseEtudiantDto response = etudiantMapper.Entity_To_DTO(etudiant);
            if (etudiant.getIdFiliere() != null) {

                FiliereDto filiere = filiereWebClient.get()
                        .uri("/v1/filieres/{id}", etudiant.getIdFiliere())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, clientResponse ->
                                Mono.error(new RuntimeException("Erreur lors de l'appel Filiere: " + clientResponse.statusCode()))
                        )
                        .bodyToMono(FiliereDto.class)
                        .blockOptional()
                        .orElse(null);

                // 3. Enrichissement
                response.setFiliere(filiere);
            }

            etudiantDtos.add(response);
        }
        return etudiantDtos;
    }

    @Override
    public ResponseEtudiantDto getEtudiantById(int id) {
        Etudiant etudiant = etudiantRepository.findById(id).orElseThrow();
        ResponseEtudiantDto response = etudiantMapper.Entity_To_DTO(etudiant);

        if (etudiant.getIdFiliere() != null) {
            FiliereDto filiere = filiereWebClient.get()
                    .uri("/v1/filieres/{id}", etudiant.getIdFiliere())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new RuntimeException("Erreur lors de l'appel Filiere: " + clientResponse.statusCode()))
                    )
                    .bodyToMono(FiliereDto.class)
                    .blockOptional()
                    .orElse(null);

            response.setFiliere(filiere);
        }
        return response;

    }

    @Override
    public ResponseEtudiantDto updateEtudiant(RequestEtudiantDto requestEtudiantDto, int id) {
        Etudiant newEtudiant = etudiantMapper.DTO_To_Entity(requestEtudiantDto);

        Etudiant existingEtudiant = etudiantRepository.findById(id).orElseThrow();

        if (newEtudiant.getNom() != null)
            existingEtudiant.setNom(newEtudiant.getNom());

        if (newEtudiant.getPrenom() != null)
            existingEtudiant.setPrenom(newEtudiant.getPrenom());

        if (newEtudiant.getCne() != null)
            existingEtudiant.setCne(newEtudiant.getCne());

        if (newEtudiant.getIdFiliere() != null)
            existingEtudiant.setIdFiliere(newEtudiant.getIdFiliere());

        Etudiant updatedEtudiant = etudiantRepository.save(existingEtudiant);

        return etudiantMapper.Entity_To_DTO(updatedEtudiant);

    }

    @Override
    public ResponseEtudiantDto deleteEtudiant(int id) {
        etudiantRepository.deleteById(id);
        return null;
    }
}
