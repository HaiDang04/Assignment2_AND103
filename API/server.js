// server.js
const express = require("express");
const app = express();
const port = 3000;
const path = require("path");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const carModel = require("./carModel");
const uri = require("./COMMON").uri;
const upload = require("./upload");
const { log } = require("console");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});

mongoose.connect(uri, {
  bufferCommands: false,
  serverSelectionTimeoutMS: 5000,
  socketTimeoutMS: 45000,
});

app.get("/lay", async (req, res) => {
  try {
    await mongoose.connect(uri);
    const cars = await carModel.find();
    console.log(cars);
    res.json({
      "status": 200,
      "messenger": "Danh sách xe",
      "data": cars
  })
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      data: [],
    });
  }
});
app.post("/them", async (req, res) => {
  try {
    const newCar = new carModel({
      tenXe: req.body.tenXe,
      gia: req.body.gia,
      loaiXe: req.body.loaiXe,
    });

    const result = await newCar.save();

    if (result) {
      res.json({
        status: 200,
        messenger: "Thêm thành công",
        data: result,
      });
    } else {
      res.status(400).json({
        status: 400,
        messenger: "Lỗi, thêm không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      data: [],
    });
  }
});
app.post("/them-co-anh", upload.single("anh"), async (req, res) => {
  const {file} = req
  const imageUrl = file ? `${req.protocol}://localhost:${port}/uploads/${file.filename}` : "";
  try {
    const newCar = new carModel({
      tenXe: req.body.tenXe,
      gia: req.body.gia,
      anh: imageUrl,
      loaiXe: req.body.loaiXe,
    });

    const result = await newCar.save();
    console.log(JSON.stringify(result));
    if (result) {
      res.json({
        status: 200,
        messenger: "Thêm thành công",
        data: result,
      });
    } else {
      res.status(400).json({
        status: 400,
        messenger: "Lỗi, thêm không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      data: [],
    });
  }
});

app.delete("/xoa/:id", async (req, res) => {
  try {
    await mongoose.connect(uri);
    const { id } = req.params;
    const result = await carModel.findByIdAndDelete(id);
    if (result) {
      res.json({
        status: 200,
        messenger: "Xóa thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi,Xóa không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});
app.put("/cap-nhat/:id", upload.single("anh"), async (req, res) => {
  const {file} = req
  const imageUrl = file ? `${req.protocol}://localhost:${port}/uploads/${file.filename}` : "";
  try {
    await mongoose.connect(uri);
    const { id } = req.params;
    const data = req.body;
    const updatecar = await carModel.findById(id);
    let result = null;
    if (updatecar) {
      updatecar.tenXe = data.tenXe ?? updatecar.tenXe;
      updatecar.gia = data.gia ?? updatecar.gia;
      updatecar.loaiXe = data.loaiXe ?? updatecar.loaiXe;
      updatecar.anh = imageUrl

      result = await updatecar.save();
    }
    if (result) {
      res.json({
        status: 200,
        messenger: "Cập nhật thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, cập nhật không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      data: [],
    });
  }
});
app.get('/tim-kiem-theo-ten', async (req, res) => {
  try {
    const key = req.query.key;
    if (!key || typeof key !== 'string') { 
      return res.status(400).json({
        status: 400,
        messenger: "Bad Request: Invalid search key",
        data: [],
      });
    }

    const cars = await carModel.find({ tenXe: { "$regex": key, "$options": "i" } });
    console.log(key)
    if (cars) {
      res.json({
          "status": 200,
          "messenger": "Tìm kiếm thành công",
          "data": cars
      });
    } else {
      res.json({
          "status": 400,
          "messenger": "Lỗi, tìm kiếm không thành công",
          "data": []
      });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      cars: [],
    });
  }
});
app.get('/sap-xep', async (req, res) => {
  try {
    let sortDirection = 1; 
    const sortParam = req.query.sort;
    if (sortParam && sortParam === 'desc') {
      sortDirection = -1;
    }
    const cars = await carModel.find().sort({ gia: sortDirection });
    res.json({
      status: 200,
      messenger: "Tìm kiếm và sắp xếp thành công",
      data: cars,
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: "Internal Server Error",
      cars: [],
    });
  }
});



module.exports = app;
