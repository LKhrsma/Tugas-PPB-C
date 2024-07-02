#! /bin/bash

# Set environment variables
export FLASK_APP=app.py
export FLASK_ENV=production
# Run the Flask application with Gunicorn
gunicorn --bind 192.168.18.252:8000 app:app