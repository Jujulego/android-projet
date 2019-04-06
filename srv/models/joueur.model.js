// Importations
import mongoose, { Schema } from "mongoose";

const { ObjectId } = Schema.Types;

// Schema
const Joueur = Schema({
    nom:    { type: String, required: true },
    prenom: { type: String, required: true }
}, { timestamps: true });

// Model
export default mongoose.model('Joueur', Joueur);