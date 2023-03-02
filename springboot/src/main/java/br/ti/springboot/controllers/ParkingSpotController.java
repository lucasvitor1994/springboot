package br.ti.springboot.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ti.springboot.dtos.ParkingSpotDto;
import br.ti.springboot.model.ParkingSpotModel;
import br.ti.springboot.services.ParkingSpotService;
import lombok.var;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	@Autowired
	ParkingSpotService parkingSpotService;
	
	
	@PostMapping("saveParkingSpot")
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
		if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use");
			
		}
		if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Spot Number is already in use");
			
		}
		if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
	    }
		
		var parkingSpotModel = new ParkingSpotModel();
//		convertendo de DTO para model
	    BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
	}
	
	@GetMapping("listParkingSpot")
	public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpot(){
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
	}

	@GetMapping("listParkingSpotById/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id")UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findById(id));
//		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
	}
	@DeleteMapping("deleteParkingSpotById/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id")UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
		}
		parkingSpotService.delete(parkingSpotModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deletede successfully");
	}
	
	@PutMapping("updateParkingSpotById/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id")UUID id,
													@RequestBody @Valid ParkingSpotDto parkingSpotDto){
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ParkingSpot not found");
		}
//		possivel realziar da seguinte forma em outras versoes do java
//		
//		var parkingSpotModel = parkingSpotModelOptional.get();
//		parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
//		parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
//		parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
//		parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
//		parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
//		parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
//		parkingSpotModel.setApartment(parkingSpotDto.getApartment());
//		parkingSpotModel.setBlock(parkingSpotDto.getBlock());
//		chamando instancia e usando o beanutius
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
//		peda os dados de id e de data de registro e sena novamente no banco para não ocorrer altraçao
		parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
		parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
	
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));

	}
				
	
	@GetMapping("/")
	public String index() {
		return "dssdsd";
		
	}

}
