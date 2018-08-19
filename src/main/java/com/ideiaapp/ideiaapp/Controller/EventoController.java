package com.ideiaapp.ideiaapp.Controller;

import com.ideiaapp.ideiaapp.Model.Convidado;
import com.ideiaapp.ideiaapp.Model.Evento;
import com.ideiaapp.ideiaapp.Repository.ConvidadoRepository;
import com.ideiaapp.ideiaapp.Repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value="/cadastrarEvento", method = RequestMethod.GET)
    public String form(){
        return "evento/formEvento";
    }

    @RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST)
    public String formPost(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarEvento";
        }

        er.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento adicionado com sucesso!");
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView modelAndView = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        modelAndView.addObject("eventos", eventos);
        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("id") long id) {
        Evento evento = er.findById(id);
        ModelAndView modelAndView = new ModelAndView("evento/detalhesEvento");
        modelAndView.addObject("evento", evento);
        System.out.println("Evento " + evento);
        Iterable<Convidado> convidados = cr.findByEvento(evento);
        modelAndView.addObject("convidados", convidados);

        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("id") long id, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/{id}";
        }
        Evento evento = er.findById(id);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/{id}";
    }

    @RequestMapping(value = "/deletarEvento")
    public String deletarEvento(Long id, RedirectAttributes attributes) {
        Evento evento = er.findById(id);
        Iterable<Convidado> convidados = cr.findByEvento(evento);

        er.delete(evento);
        cr.deleteAll(convidados);
        attributes.addFlashAttribute("mensagem", "Evento foi removido!");
        return "redirect:/eventos";
    }

    @RequestMapping(value = "/deletarConvidado")
    public String deletarConvidado(String rg) {
        Convidado convidado = cr.findByRg(rg);
        System.out.println("RG convidado: " + rg);
        System.out.println("Evento id:" + convidado.getEvento());
        Evento evento = convidado.getEvento();

        cr.delete(convidado);
        long idDoEvento = evento.getId();
        String id = "" + idDoEvento;

        return "redirect:/" + id;
    }

}
