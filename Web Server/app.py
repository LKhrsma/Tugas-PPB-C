from flask import Flask, request, jsonify, send_from_directory
from werkzeug.middleware.proxy_fix import ProxyFix
import os
from flask_sslify import SSLify

app = Flask(__name__)
context = ('cert.pem', 'key.pem')

# Gunakan ProxyFix jika di belakang reverse proxy
app.wsgi_app = ProxyFix(app.wsgi_app)


# Direktori untuk menyimpan gambar yang diunggah
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Endpoint untuk mengunggah gambar dari Android
@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400
    if file:
        filename = file.filename
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return jsonify({"message": "File uploaded successfully!"}), 200

@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

# get url and filename
@app.route('/geturl', methods=['GET'])
def get_url():
    # get file name from folder uploads
    filename = os.listdir(app.config['UPLOAD_FOLDER'])[0]
    file_url = f"http://192.168.18.252:8000/uploads/{filename}"
    return jsonify({"url": file_url}), 200

# remove image
@app.route('/remove', methods=['GET'])
def remove_image():
    filename = os.listdir(app.config['UPLOAD_FOLDER'])[0]
    os.remove(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    return jsonify({"message": "Image removed successfully!"}), 200


if __name__ == "__main__":
    # app.run(host='0.0.0.0', port=8000, debug=True)
    app.run(host='192.168.18.252', port=8000)

    

