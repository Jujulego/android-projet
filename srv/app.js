// Importations
import express from 'express';
import mongoose from 'mongoose';
import morgan from 'morgan';

// Connect to mongoose
mongoose.connect("mongodb://localhost/android", { useNewUrlParser: true })
    .then(() => console.log("Connected to MongoDB"));

// Init express
const app = express();

// Middlewares
app.use(morgan('dev'));

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

// Routes


export default app;