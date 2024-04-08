const mongoose = require("mongoose");
const CarSchema = new mongoose.Schema({
  tenXe: {
    type: String,
    require: true,
  },
  gia: {
    type: Number,
    require: true,
  },
  anh: {
    type: String,
  },
  loaiXe: {
    type: String,
    require: true,
  },
});

const CarModel = new mongoose.model("car", CarSchema);
module.exports = CarModel;
