/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.laboratoire.controller;

import com.example.laboratoire.model.Section;
import com.example.laboratoire.model.Test;
import com.example.laboratoire.model.TestEffectue;
import com.example.laboratoire.repository.TestRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author CHRISTIAN
 */

@RestController
public class TestController {
    
    @Autowired
    TestRepository testRepo;
    
   @RequestMapping("/tests")
   public List<Test> index(){
       
       List<Test> list = new ArrayList();
       
       // Removing samples from tests returned to avoid recursion
       Iterable<Test> tests = testRepo.findByStatutVie(true);
       for(Test s: tests){
           List<TestEffectue> tes = s.getTestsEffectues();
           s.setTestsEffectues(null);
           for(TestEffectue te: tes){
               //te.setTest(null);
               te.getSample().setTestsEffectues(null);
           }
           s.setTestsEffectues(tes);
           
//           tes.stream().map((te) -> {
//               te.setSample(null);
//               return te;
//           }).forEachOrdered((te) -> {
//               te.getTest().setTestsEffectues(tes);
//           });
            list.add(s);
       }
     
      // samples.forEach(list::add);
       
       return list;
   }
   
   @RequestMapping("/tests/{id}")
   public Test show(@PathVariable Long id){
       
       return testRepo.findById(id).get();
   }
   
//   @RequestMapping("/{sampleTypeId}/tests")
//   public List<Test> getBySampleType(@PathVariable("sampleTypeId") int sampleTypeId){
//       
//       return testRepo.findBySampleTypeId(sampleTypeId).get();
//   }
   
   @RequestMapping(value="sections/{sectionId}/tests", method = RequestMethod.POST)
   public Test store(@RequestBody Test test, @PathVariable("sectionId") int sectionId){
       
       
       return testRepo.save(
               test.setStatutVie(true)
                  .setSection(new Section(sectionId))
                  .setCreatedOn(new Date())
                  .setUpdatedOn(new Date())
       );
   }
   
   @RequestMapping(value="/tests/{id}", method = RequestMethod.PATCH)
   public Test update(@RequestBody Test test){
       
       return testRepo.save(test.setUpdatedOn(new Date()));
   }
   

   @RequestMapping(value="/tests/{id}", method = RequestMethod.DELETE)
   public Test delete(@PathVariable Long id){
       
       return testRepo.findById(id).get()
               .setStatutVie(false);
       
   }
}
