// Ilustraciones del material "Senas que rompen barreras" (@LSP_EDUCACION)
import logo from '../assets/img/logo.webp'
import logoGrande from '../assets/img/logo-grande.webp'
import ninoSaluda from '../assets/img/nino-saluda.webp'
import ninaSaluda from '../assets/img/nina-saluda.webp'
import ninoGuino from '../assets/img/nino-guino.webp'
import nino from '../assets/img/nino.webp'
import ninoGracias from '../assets/img/nino-gracias.webp'
import ninaFeliz from '../assets/img/nina-feliz.webp'
import ninaSenia from '../assets/img/nina-senia.webp'
import gato from '../assets/img/gato.webp'
import gatoSentado from '../assets/img/gato-sentado.webp'
import gatoFeliz from '../assets/img/gato-feliz.webp'
import gatoCurioso from '../assets/img/gato-curioso.webp'
import flores from '../assets/img/flores.webp'
import parque from '../assets/img/parque.webp'
import fotoAula from '../assets/img/foto-aula.webp'
import fotoSalud from '../assets/img/foto-salud.webp'
import fotoTrabajo from '../assets/img/foto-trabajo.webp'
import fotoCongreso from '../assets/img/foto-congreso.webp'
import fotoInterprete from '../assets/img/foto-interprete.webp'
import consejoHombro from '../assets/img/consejo-hombro.webp'
import consejoLuz from '../assets/img/consejo-luz.webp'
import consejoEscribir from '../assets/img/consejo-escribir.webp'
import consejoTurnos from '../assets/img/consejo-turnos.webp'
import consejoSenas from '../assets/img/consejo-senas.webp'
import consejoLibro from '../assets/img/consejo-libro.webp'

export const img = {
  logo,
  logoGrande,
  ninoSaluda,
  ninaSaluda,
  ninoGuino,
  nino,
  ninoGracias,
  ninaFeliz,
  ninaSenia,
  gato,
  gatoSentado,
  gatoFeliz,
  gatoCurioso,
  flores,
  parque,
  fotoAula,
  fotoSalud,
  fotoTrabajo,
  fotoCongreso,
  fotoInterprete,
  consejoHombro,
  consejoLuz,
  consejoEscribir,
  consejoTurnos,
  consejoSenas,
  consejoLibro,
}

/** Imagenes de relleno para contenidos que todavia no tienen foto propia. */
const fotos = [fotoAula, fotoInterprete, fotoSalud, fotoTrabajo, fotoCongreso, consejoSenas]
const personajes = [ninoSaluda, ninaSenia, gatoSentado, ninoGuino, ninaFeliz, gatoFeliz]

export function fotoRelleno(indice: number) {
  return fotos[Math.abs(indice) % fotos.length]
}

export function personajeRelleno(indice: number) {
  return personajes[Math.abs(indice) % personajes.length]
}
