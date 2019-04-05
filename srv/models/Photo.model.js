// Importations
import mongoose, { Schema } from "mongoose";

const { ObjectId } = Schema.Types;

// Schema
const Photo = Schema({
    photo: { type: String,   required: true },
    match: { type: ObjectId, required: true },
    date:  { type: Date },
});

// Model
export default mongoose.model('Photo', Photo);