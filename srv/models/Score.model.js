// Importations
import mongoose, { Schema } from "mongoose";

const { ObjectId } = Schema.Types;

// Schema
const Score = Schema({
    score:  { type: Number, default: 0 },
    match:  { type: ObjectId, required: true },
    joueur: { type: ObjectId, required: true }
});

// Model
export default mongoose.model('Score', Score);