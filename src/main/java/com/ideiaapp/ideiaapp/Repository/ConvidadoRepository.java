package com.ideiaapp.ideiaapp.Repository;

import com.ideiaapp.ideiaapp.Model.Convidado;
import com.ideiaapp.ideiaapp.Model.Evento;
import org.springframework.data.repository.CrudRepository;

public interface ConvidadoRepository extends CrudRepository<Convidado, String> {
    Iterable<Convidado> findByEvento(Evento evento);
    Convidado findByRg(String rg);
}
