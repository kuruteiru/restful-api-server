package utb.fai.RESTAPIServer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET /users
    @GetMapping("/users")
    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }

    // GET /getUser?id=...
    @GetMapping("/getUser")
    public ResponseEntity<MyUser> getUserById(@RequestParam Long id) {
        Optional<MyUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST /createUser
    @PostMapping("/createUser")
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser newUser) {
        if (!newUser.isUserDataValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            MyUser savedUser = userRepository.save(newUser);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT /editUser?id=... (Body contains user data)
    @PutMapping("/editUser")
    public ResponseEntity<MyUser> editUser(@RequestParam Long id, @RequestBody MyUser updatedData) {
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!updatedData.isUserDataValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            updatedData.setId(id); 
            MyUser savedUser = userRepository.save(updatedData);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE /deleteUser?id=...
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        if (!userRepository.existsById(id)) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE /deleteAll
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
