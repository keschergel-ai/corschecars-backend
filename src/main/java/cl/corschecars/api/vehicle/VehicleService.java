package cl.corschecars.api.vehicle;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleService {
    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {
        return repository.findAll().stream()
                .map(VehicleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse findById(Long id) {
        return VehicleResponse.from(findVehicle(id));
    }

    public VehicleResponse create(VehicleRequest request) {
        Vehicle vehicle = new Vehicle(
                request.nombre(),
                request.precio(),
                request.categoria(),
                request.imagen(),
                request.descripcion());
        return VehicleResponse.from(repository.save(vehicle));
    }

    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = findVehicle(id);
        vehicle.update(
                request.nombre(),
                request.precio(),
                request.categoria(),
                request.imagen(),
                request.descripcion());
        return VehicleResponse.from(repository.save(vehicle));
    }

    public void delete(Long id) {
        repository.delete(findVehicle(id));
    }

    private Vehicle findVehicle(Long id) {
        return repository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
    }
}
