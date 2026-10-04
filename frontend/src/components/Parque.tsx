import type { ReactNode } from 'react'

/**
 * Escenario de parque (cielo, edificios, arbustos, camino y pasto) dibujado
 * con SVG/CSS, igual al fondo de las diapositivas del material.
 */
export default function Parque({ children }: { children?: ReactNode }) {
  return (
    <div className="parque">
      <svg className="parque__ciudad" viewBox="0 0 1200 220" preserveAspectRatio="xMidYMax slice" aria-hidden="true">
        <g fill="#a9cbe6">
          <rect x="20" y="70" width="60" height="150" />
          <rect x="90" y="110" width="45" height="110" />
          <rect x="150" y="40" width="70" height="180" />
          <rect x="235" y="100" width="50" height="120" />
          <rect x="300" y="60" width="40" height="160" />
          <rect x="350" y="20" width="35" height="200" />
          <rect x="400" y="90" width="80" height="130" />
          <rect x="495" y="50" width="55" height="170" />
          <rect x="560" y="120" width="60" height="100" />
          <rect x="630" y="30" width="50" height="190" />
          <rect x="690" y="80" width="70" height="140" />
          <rect x="775" y="110" width="40" height="110" />
          <rect x="825" y="45" width="60" height="175" />
          <rect x="900" y="95" width="75" height="125" />
          <rect x="985" y="25" width="45" height="195" />
          <rect x="1040" y="75" width="65" height="145" />
          <rect x="1115" y="105" width="80" height="115" />
        </g>
        <g fill="#c9e6f4" opacity="0.9">
          {Array.from({ length: 60 }).map((_, i) => (
            <rect key={i} x={30 + ((i * 97) % 1150)} y={90 + ((i * 53) % 110)} width="8" height="10" />
          ))}
        </g>
      </svg>
      <div className="parque__arbustos" aria-hidden="true" />
      <div className="parque__camino" aria-hidden="true" />
      <div className="parque__pasto" aria-hidden="true" />
      <div className="parque__contenido">{children}</div>
    </div>
  )
}
