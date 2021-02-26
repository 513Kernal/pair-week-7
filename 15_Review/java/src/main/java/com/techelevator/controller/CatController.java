package com.techelevator.controller;

import com.techelevator.model.*;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CatController {

    private CatCardDAO catCardDao;
    private CatFactService catFactService;
    private CatPicService catPicService;

    public CatController(CatCardDAO catCardDao, CatFactService catFactService, CatPicService catPicService) {
        this.catCardDao = catCardDao;
        this.catFactService = catFactService;
        this.catPicService = catPicService;
    }

    @RequestMapping(path ="", method = RequestMethod.GET)
    public List<CatCard> getAllCards(){
        return catCardDao.list();
    }

    @RequestMapping(path ="/{id}", method = RequestMethod.GET)
    public CatCard getCard(@PathVariable long id) throws CatCardNotFoundException {
        return catCardDao.get(id);
    }

    @RequestMapping(path = "/random", method = RequestMethod.GET)
    public CatCard generateNewRandomCard() {
        CatCard card = new CatCard();

        CatFact fact = catFactService.getFact();
        CatPic pic = catPicService.getPic();

        card.setCatFact(fact.getText());
        card.setImgUrl(pic.getFile());

        return card;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void save(@Valid @RequestBody CatCard card){
        catCardDao.save(card);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void udpateCard(@Valid @RequestBody CatCard card, @PathVariable long id) throws CatCardNotFoundException {
        if (!catCardDao.update(id, card)) {
            throw new CatCardNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteCard(@PathVariable long id) throws CatCardNotFoundException {
        if (!catCardDao.delete(id)) {
            throw new CatCardNotFoundException();
        }
    }


}
