# vim: set ts=2 sw=2 expandtab:
import webapp2
import os
import datetime
from google.appengine.ext import db
from google.appengine.api import users
from webapp2_extras.routes import RedirectRoute
from google.appengine.ext.webapp import template

#
# Database model representing the data-store "schema"
#
class Student(db.Model):
  name = db.StringProperty(required=True)
  role = db.StringProperty(required=True,
                           choices=set(["student", "classrep"]))
  enrolled_date = db.DateProperty()
  induction_completed = db.BooleanProperty(indexed=False)
  email = db.StringProperty()


#
# Top level request handler
#
class ApplicationController(webapp2.RequestHandler):
  admin = None

  def render_response(self, _template, templateValues={}):
    # Renders a template and writes the result to the response.
    path = os.path.join(os.path.dirname(__file__), _template)
    self.response.out.write(template.render(path, templateValues))

  def authenticate(self):
    if not self.admin:
      self.admin = users.get_current_user()
      if not self.admin:
        self.redirect(users.create_login_url(self.request.uri))

  def index(self):
    self.redirect('/students')

#
# Student request handling
#
class StudentsController(ApplicationController):
  def index(self):
    self.authenticate()
    students = db.GqlQuery("SELECT * FROM Student")
    return self.render_response("students/index.html", {'students': students})

  def new(self):
    self.authenticate()
    return self.render_response("students/new.html")

  def create(self):
    self.authenticate()
    student = Student(name=self.request.get('name'), role='student')
    student.put()
    self.redirect('/students')


#
# Initialise the routing
#
def createRoutes():
  applicationRoutes = []

  #
  # Update the following array to add/change routes
  routeInfo = [
    ('application.index', 'GET', '/', ApplicationController, 'index'),
    ('students.index', 'GET', '/students', StudentsController, 'index'),
    ('students.new', 'GET', '/students/new', StudentsController, 'new'),
    ('students.create', 'POST', '/students', StudentsController, 'create'),
    ('students.show', 'GET', '/students/<id:\d+>', StudentsController, 'show'),
  ]

  for name, methods, pattern, handlerClass, handlerMethod in routeInfo:
    # Allow a single string, but this has to be changed to a list.
    # None here means any method
    if isinstance(methods, basestring):
      methods = [methods]

    # Create the route
    route = RedirectRoute(name=name, template=pattern, methods=methods, handler=handlerClass, handler_method=handlerMethod)

    # Add the route to the public list
    applicationRoutes.append(route)

  return applicationRoutes

#
# Create the app
#
app = webapp2.WSGIApplication(routes=createRoutes(), debug=True)

