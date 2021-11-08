package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }
    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer id,@RequestParam int page){
        Pageable pageable=PageRequest.of(page,10);
        return studentRepository.findAllByGroup_FacultyId(id,pageable);
    }
    //4. GROUP OWNER
    @GetMapping("/forGroup/{id}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer id,@RequestParam int page){
        Pageable pageable=PageRequest.of(page,10);
        return studentRepository.findAllByGroupId(id,pageable);
    }
    @PostMapping
    public String addStudentForMInistry(@RequestBody StudentDto studentDto){
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAdressId());
        if (!optionalAddress.isPresent())
            return "BUnday id li adress topilmadi";
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent())
            return "Bunday id li group topilmadi";
        List<Subject> subjectList = subjectRepository.findAllById(studentDto.getSubjectIdList());
        Student student=new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "Bajarildi";
    }
    @PutMapping("/{id}")
    public String editStudent(@RequestBody StudentDto studentDto,@PathVariable Integer id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent())
            return "Bunday id li student topilmadi";
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAdressId());
        if (!optionalAddress.isPresent())
            return "BUnday id li adress topilmadi";
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent())
            return "Bunday id li group topilmadi";
        List<Subject> subjectList = subjectRepository.findAllById(studentDto.getSubjectIdList());
        Student student = optionalStudent.get();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "Bajarildi";
    }
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
        try {
            studentRepository.deleteById(id);
            return "Bajarilmadi";
        } catch (Exception e) {
            return "Xatolik yuzaga keldi";
        }
    }





}
