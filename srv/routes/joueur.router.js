// Importations
import express, { Router } from 'express';

import joueurs from '../controllers/joueurs.controller';
import utils from '../utils';

// Router
const router = new Router();

// Routes
router.get('/all', function (req, res) {
    joueurs.all(res, (joueurs) => res.json(joueurs));
});

router.put('/', utils.has_params(["nom", "prenom"], function (req, res) {
    joueurs.insert(req.body, res, (joueur) => res.json(joueur));
}));

router.route('/:id')
    .get(function (req, res) {
        joueurs.get(req.params.id, res, (joueur) => res.json(joueur));
    })
    .post(function (req, res) {
        joueurs.update(req.params.id, req.body, res, (joueur) => res.json(joueur));
    })
    .delete(function (req, res) {
        joueurs.remove(req.params.id, res, () => res.json({ msg: `Joueur ${req.params.id} deleted.`}));
    });

export default router;