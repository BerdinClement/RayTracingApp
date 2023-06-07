const express = require('express');
const fileUpload = require('express-fileupload');
const exec = require('node:child_process');
const cors = require('cors');
const multer = require("multer");
const fs = require('fs');


const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, '../bin/');
    },

    filename: function(req, file, cb) {
        cb(null, "simple.txt");
    }
});

const upload = multer({ storage: storage })

const port = 5394;
const app = express();

app.use(express.json());
app.use(cors());

app.post('/upload', upload.single('uploadedfile'), (req, res) => {
    if (!req.file) {
        res.status(400).json({
            status: "error",
            message: "No file uploaded"
        });
    }
    const { filename: image } = req.file;
    res.status(200).json({
        status: "success",
        message: "File uploaded successfully",
    });
});

app.post('/traitement', (req, res) => {

    const { nbRays, nbThreads } = req.body;

    console.log(nbRays, nbThreads)

    exec.exec(`cd /app/src/bin/ && java lr.LR ${nbRays} ${nbThreads}`, (error, stdout, stderr) => {
        if (error) {
            console.log(`error: ${error.message}`);
            res.status(400).json({
                status: "error",
                message: "Error lors du traitement"
            });
        } else if (stderr) {
            console.log(`stderr: ${stderr}`);
            res.status(400).json({
                status: "error",
                message: "Error lors du traitement"
            });
        }
        const imagePath = '/app/src/bin/image1.png';
        const absPath = imagePath;

        console.log(`stdout: ${stdout}`);

        fs.readFile(imagePath, (err, data) => {
            if (err) {
                console.error(err);
                return res.status(500).json({
                    status: "error",
                    message: "Server error"
                });
            } else {
                const img = Buffer.from(data, 'base64');
                res.writeHead(200, {
                    'Content-Type': 'image/png',
                    'Content-Length': img.length
                });
                res.end(img);
                console.log('File sent successfully')
            }
        });
    });
})

app.get('/', (req, res) => {
    res.send('Hello World!')
})


app.listen(port, () => {
    console.log(`Server listening on port: ${port}`);
    exec.exec(`cd src/src && javac lr/format/simple/*.java lr/format/*.java lr/*.java -d ../bin`, (error, stdout, stderr) => {
        if (error) {
            console.log(`error: ${error.message}`);
        } else if (stderr) {
            console.log(`stderr: ${stderr}`);
        }
        console.log(`stdout: ${stdout}`);
    });
});

module.exports = app;