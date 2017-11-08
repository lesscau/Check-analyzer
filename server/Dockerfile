FROM tiangolo/uwsgi-nginx-flask:python3.6

# Copy over our requirements.txt file
COPY requirements.txt /tmp/

# Upgrade pip and install required python packages
RUN pip install -U pip
RUN pip install -r /tmp/requirements.txt
RUN pip install mysqlclient

# Set secret key for application
ARG FLASK_APP_SECRET_KEY
ENV FLASK_APP_SECRET_KEY ${FLASK_APP_SECRET_KEY:-}

# Set database path for application
ARG DATABASE_URL
ENV DATABASE_URL ${DATABASE_URL:-}

# Copy application
COPY . /app