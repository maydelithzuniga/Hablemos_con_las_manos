package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.entity.Donacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Pasarela de desarrollo: no cobra nada, solo devuelve la URL de retorno del
 * frontend con la referencia. El pago se confirma llamando manualmente al
 * webhook (POST /api/donaciones/webhook) o desde el panel.
 */
@Service
public class PasarelaPagoSimulada implements PasarelaPagoService {

    private final String urlRetorno;

    public PasarelaPagoSimulada(@Value("${app.donaciones.url-retorno}") String urlRetorno) {
        this.urlRetorno = urlRetorno;
    }

    @Override
    public String crearPago(Donacion donacion) {
        return urlRetorno + "?referencia=" + donacion.getReferencia();
    }
}
