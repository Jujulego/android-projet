// Importations
import Joueur from "../models/joueur.model";
import utils from "../utils";

// Utils
function msg500(verb, id = null) {
    if (id != null) {
        return `Internal error while ${verb} joueur ${id}`
    } else {
        return `Internal error while ${verb} joueur(s)`
    }
}

function msg404(id) {
    return `Joueur ${id} not found`
}

// Méthodes
// - accès
export function all(res, cb) {
    // Query
    Joueur.find()
        .exec(utils.check_error_500(res, msg500("getting"), cb));
}

export function get(id, res, cb) {
    // Query
    Joueur.findById(id)
        .exec(utils.check_error_404(res, msg404(id), msg500("getting", id), cb));
}

export function search(filter, res, cb) {
    // Query
    Joueur.find(filter)
        .exec(utils.check_error_500(res, msg500("getting"), cb))
}

export function aggregate(pipeline, res, cb) {
    // Query
    Joueur.aggregate(pipeline)
        .exec(utils.check_error_500(res, msg500("computing on"), cb))
}

export function count(res, cb) {
    // Query
    Joueur.estimatedDocumentCount()
        .exec(utils.check_error_500(res, msg500("computing on"), cb));
}

// - modification
export function insert({ nom, prenom }, res, cb) {
    // Query
    const joueur = new Joueur({ nom, prenom });
    joueur.save(utils.check_error_500(res, msg500("creating"), cb));
}

export function save(joueur, res, cb) {
    joueur.save(utils.check_error_500(res, msg500("updating"), cb));
}

export function remove(id, res, cb) {
    // Query
    Joueur.findByIdAndDelete(id)
        .exec(utils.check_error_500(res, msg500("deleting", id), cb));
}

export function update(id, { nom, prenom }, res, cb) {
    // Query album
    get(id, res, (joueur) => {
        // Update
        if (nom)    joueur.nom    = nom;
        if (prenom) joueur.prenom = prenom;

        save(joueur, res, cb);
    });
}

export default { all, get, search, aggregate, count, insert, save, remove, update };