// Importations
import mongoose, { Schema } from "mongoose";

// Schema
export default new Schema({
    type: {
        type: String,
        enum: ['Point'],
        required: true
    },
    coordinates: {
        type: [Number],
        required: true
    }
});