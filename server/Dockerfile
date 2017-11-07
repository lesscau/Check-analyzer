FROM tiangolo/uwsgi-nginx-flask:python3.6

# Copy over our requirements.txt file
COPY requirements.txt /tmp/

# Upgrade pip and install required python packages
RUN pip install -U pip
RUN pip install -r /tmp/requirements.txt

# Set secret key for application
ARG FLASK_APP_SECRET_KEY
RUN if [ $FLASK_APP_SECRET_KEY!="" ]; then export FLASK_APP_SECRET_KEY=$FLASK_APP_SECRET_KEY; fi

# Set database path for application
ARG DATABASE_URL
RUN if [ $DATABASE_URL!="" ]; then export DATABASE_URL=$DATABASE_URL; fi

# Copy application
COPY . /app