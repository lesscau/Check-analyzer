FROM dan0n/flask-alpine

# Copy application
COPY . /app

# Set secret key for application
ARG FLASK_APP_SECRET_KEY
# Set database path for application
ARG DATABASE_URL

# Set env variables from ARG
ENV FLASK_APP_SECRET_KEY=${FLASK_APP_SECRET_KEY:-} \
    DATABASE_URL=${DATABASE_URL:-}

# Upgrade pip and install required python packages
RUN pip install -U pip \
    && pip install -r requirements.txt \
# Upgrade database
    && python db.py upgrade
