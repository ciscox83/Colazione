from flask import Flask
from flask import request
from google.appengine.ext import db

app = Flask(__name__)
app.config['DEBUG'] = True

# Note: We don't need to call run() since our application is embedded within
# the App Engine WSGI application server.


@app.route('/')
def hello():
    """Return a friendly HTTP greeting."""
    return 'Hello World!'

@app.route('/sentences', methods=['GET', 'PUT'])
def sentences():
    if request.method == 'GET':
        """Return the list of all the sentences stored on the server."""
        sentences = File.get_or_insert('sentences', content='')
        return sentences.content
    else:
        """Replace the list of the sentences stored on the server."""
        newSentences = request.data
        sentences = File.get_or_insert('sentences', content='')
        sentences.content = newSentences
        sentences.put()
        return "OK"
        

@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, nothing at this URL.', 404


class File(db.Model):
     content = db.TextProperty()
