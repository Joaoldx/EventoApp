package com.ideiaapp.ideiaapp.Repository;

import com.ideiaapp.ideiaapp.Model.Evento;
import org.springframework.data.repository.CrudRepository;

public interface EventoRepository extends CrudRepository<Evento, String> {
    Evento findById(long id);
}
