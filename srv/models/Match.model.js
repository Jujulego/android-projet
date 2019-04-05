// Importations
import mongoose, { Schema } from 'mongoose';
import Point from './Point.schema';

const { ObjectId } = Schema.Types;

// Schema
const Match = Schema({
    titre:       { type: String, required: true },
    description: { type: String },
    date:        { type: Date },
    location:    { type: Point }
});

Match.index({ location: '2dsphere' });

// Model
export default mongoose.model('Match', Match);